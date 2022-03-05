# search microservice

"""
Search microservice
"""

import atexit
from datetime import datetime, timedelta
import logging
import os
import sys
import re
import time

from flask import Flask, jsonify, request
import bleach

import redis
from redisearch import Client, Query

def create_app():
    """Flask application factory to create instances
    of the Userservice Flask App
    """
    app = Flask(__name__)

    # Disabling unused-variable for lines with route decorated functions
    # as pylint thinks they are unused
    # pylint: disable=unused-variable
    @app.route('/ready', methods=['GET'])
    def readiness():
        """
        Readiness probe
        """
        return 'ok', 200

    @app.route('/search', methods=['POST'])
    def search():
        """Search
        """

        app.logger.debug('Request received.')

        # parse search terms in form data
        req = {k: bleach.clean(v) for k, v in request.form.items()}
        search_term = req['search']
        limit = int(req['limit']) if 'limit' in req else 0
        offset = int(req['offset']) if 'offset' in req else 0

        # query redis
        query = Query(search_term).paging(offset, limit) if limit > 0 else Query(search_term)
        res = client.search(query)

        # marshall results
        results = [{'sku'         : res.docs[i].sku,     # otherwise fatal
                    'title'       : getattr(res.docs[i], 'title', ''),
                    'description' : getattr(res.docs[i], 'description', ''),
                    'categories'  : getattr(res.docs[i], 'categories', '')
                    } for i in range(len(res.docs)) ]

        response = jsonify(results)
        response.headers.add('Access-Control-Allow-Origin', '*')
        return response, 200

    @atexit.register
    def _shutdown():
        """Executed when web app is terminated."""
        app.logger.info("Stopping search service.")


    # Set up logger
    app.logger.handlers = logging.getLogger('gunicorn.error').handlers
    app.logger.setLevel(logging.getLogger('gunicorn.error').level)
    app.logger.info('Starting search service.')

    # Configure redis connection
    host = os.environ.get('REDIS_HOSTNAME') if os.environ.get('REDIS_HOSTNAME') else "127.0.0.1"
    port = int(os.environ.get('REDIS_PORT')) if os.environ.get('REDIS_PORT') else 6380
    client = Client("ft", host=host, port=port) # redisearch full text search index
    try:
        client.redis.ping()
    except (ConnectionRefusedError, redis.exceptions.ConnectionError):
        app.logger.critical("cannot connect to redis at " + host + " on port " + str(port))
        time.sleep(5) # avoid spamming logs
        sys.exit(1)
    return app


if __name__ == "__main__":
    # Create an instance of flask server when called directly
    SEARCHSERVICE = create_app()
    SEARCHSERVICE.run()
