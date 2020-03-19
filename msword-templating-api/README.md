# reportingTools
It has total 2 microservices and 1 Vue js based web application to achieve *.docx based report generate from *.docx based template.
The api - microservice was build on Java and it has the rest api which grabs the file system details for the web application to manage docx template and manage the generated report.
The python-api - microservice was build on python which a rest api to generate numbers of docx documents from a defined docx template.

# Build Information

### docker build,run command for java-api
```
#build
docker build --tag samitkumarpatel/reportingtool-java-api:latest .

#run & test
docker run --rm -p 8081:8081 -v /Users/samit/Downloads/template/:/template -v /Users/samit/Downloads/download/:/download --link mongo-test -e mongo_host=mongo-test samitkumarpatel/reportingtool/java-api:latest

#push
docker push samitkumarpatel/reportingtool-java-api:latest

```

### docker build,run command for python-api
```
#build
docker build -t samitkumarpatel/reportingtool-python-api:latest .

#run
docker run --rm -p 5000:5000 -v /Users/samit/Downloads/template/:/template -v /Users/samit/Downloads/download/:/download -e TEMPLATE_PATH_=/template/ -e DOWNLOAD_PATH_=/download/ samitkumarpatel/reportingtool-python-api:latest

#push
docker push samitkumarpatel/reportingtool-python-api:latest
```

### docker build,run command for ui

```
#build
docker build --tag samitkumarpatel/reportingtool-ui:latest .

#run
docker run --rm -p 8080:80 samitkumarpatel/reportingtool-ui

#push
docker push samitkumarpatel/reportingtool-ui:latest

```

# deploy / start / stop the application
```
#deploy & start
ansible-playbook -i inventories/do/host.ini deploy.yml --ask-vault-pass --tags "start"

#stop
ansible-playbook -i inventories/do/host.ini deploy.yml --ask-vault-pass --tags "stop"

``` 
