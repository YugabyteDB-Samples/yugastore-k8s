FROM gcr.io/google-appengine/python

#ENV PYTHONUNBUFFERED=1
ENV INITRD No
ENV LANG en_US.UTF-8

# Create a virtualenv for the application dependencies.
RUN virtualenv -p python3.7 /env

# Set virtualenv environment variables. This is equivalent to running
# source /env/bin/activate. This ensures the application is executed within
# the context of the virtualenv and will have access to its dependencies.
ENV VIRTUAL_ENV /env
ENV PATH /env/bin:$PATH

# explicitly set a fallback log level in case no log level is defined by Kubernetes
ENV LOG_LEVEL info

# Install dependencies.
ADD requirements.txt /app/requirements.txt
RUN pip install -r /app/requirements.txt

# Add application code.
ADD *.py /app/

EXPOSE 8888
CMD gunicorn -b :8888 --threads 2 --log-level=DEBUG "searchservice:create_app()"
