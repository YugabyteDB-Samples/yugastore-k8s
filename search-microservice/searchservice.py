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

from flask import Flask, jsonify, request
import bleach

from redisearch import Client

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
        req = {k: bleach.clean(v) for k, v in request.form.items()}

        # query redis
        res = client.search(req['search'])

        # marshall results
        results = [{'sku'         : res.docs[i].sku,
                    'title'       : res.docs[i].title,
                    'description' : res.docs[i].description,
                    'categories'  : res.docs[i].categories }
                   for i in range(res.total) ]

        return jsonify(results), 200

    @atexit.register
    def _shutdown():
        """Executed when web app is terminated."""
        app.logger.info("Stopping search service.")


    # Set up logger
    app.logger.handlers = logging.getLogger('gunicorn.error').handlers
    app.logger.setLevel(logging.getLogger('gunicorn.error').level)
    app.logger.info('Starting search service.')

    # Configure database connection
    client = Client("ft", host="localhost", port=6380)
    try:
        client.redis.ping()
    except (ConnectionRefusedError, redis.exceptions.ConnectionError):
        app.logger.critical("cannot connect to redis")
        sys.exit(1)
    return app


if __name__ == "__main__":
    # Create an instance of flask server when called directly
    SEARCHSERVICE = create_app()
    SEARCHSERVICE.run()
