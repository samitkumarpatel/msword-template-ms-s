FROM python:2.7
CMD mkdir /template /download
CMD mkdir /api
COPY api.py api.py
COPY requirement.txt requirement.txt
RUN pip install -r requirement.txt
EXPOSE 5000
ENTRYPOINT ["python","api.py"]
