#!/bin/sh
if which confd > /dev/null; then
echo "confd already exists"
else
echo "confd not found"
confd_ver='0.18.2'
confd_url=" https://github.com/abtreece/confd/releases/download/v$confd_ver/confd-v$confd_ver-linux-amd64.tar.gz"
sudo wget $confd_url
sudo mkdir -p /opt/confd/bin
sudo mv confd /usr/bin/confd
sudo chmod +x /usr/bin/confd
export PATH="$PATH:/usr/bin/"
fi
confd --version
if [ "$env" = "dev" ]; then
  sed -i 's/$env/dev/g' DevOps/confd/templates/application.tmpl
  sed -i 's/$env/dev/g' DevOps/confd/conf.d/application.toml
  export AWS_DEFAULT_PROFILE=devaccess && export AWS_REGION=eu-west-3 && /bin/bash -c "confd -onetime -backend ssm --log-level debug --confdir DevOps/confd/"
  mvn clean package
  docker build -t nb-bulk-allocation:1.0 .
  docker tag nb-bulk-allocation:1.0 807185093474.dkr.ecr.eu-west-3.amazonaws.com/bulk-allocation-microservice:${BUILD_NUMBER}
  aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin 807185093474.dkr.ecr.eu-west-3.amazonaws.com
  docker push 807185093474.dkr.ecr.eu-west-3.amazonaws.com/bulk-allocation-microservice:${BUILD_NUMBER}
  sed -i "s/:1.0/:$BUILD_NUMBER/g; s/accountid/807185093474/g; s/ecrrepo/bulk-allocation-microservice/g" DevOps/nb-revola-bulk-allocation-deployment.yaml
elif [ "$env" = "prod" ]; then
  sed -i 's/$env/prod/g' DevOps/confd/templates/application.tmpl
  sed -i 's/$env/prod/g' DevOps/confd/conf.d/application.toml
  export AWS_DEFAULT_PROFILE=prodaccess && export AWS_REGION=eu-west-3 && /bin/bash -c "confd -onetime -backend ssm --log-level debug --confdir DevOps/confd/"
  mvn clean package
  docker build -t nb-bulk-allocation:1.0 .
  docker tag nb-bulk-allocation:1.0 702737140499.dkr.ecr.eu-west-3.amazonaws.com/bulk-allocation-microservice:${BUILD_NUMBER}
  aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin 702737140499.dkr.ecr.eu-west-3.amazonaws.com
  docker push 702737140499.dkr.ecr.eu-west-3.amazonaws.com/bulk-allocation-microservice:${BUILD_NUMBER}
  sed -i "s/:1.0/:$BUILD_NUMBER/g; s/accountid/702737140499/g; s/ecrrepo/bulk-allocation-microservice/g" DevOps/nb-revola-bulk-allocation-deployment.yaml
elif [ "$env" = "uat" ];then
  sed -i 's/$env/uat/g' DevOps/confd/templates/application.tmpl
  sed -i 's/$env/uat/g' DevOps/confd/conf.d/application.toml
  export AWS_DEFAULT_PROFILE=devaccess && export AWS_REGION=eu-west-3 && /bin/bash -c "confd -onetime -backend ssm --log-level debug --confdir DevOps/confd/"
  mvn clean package
  docker build -t nb-bulk-allocation:1.0 .
  docker tag nb-bulk-allocation:1.0 807185093474.dkr.ecr.eu-west-3.amazonaws.com/uat-bulk-allocation-microservice:${BUILD_NUMBER}
  aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin 807185093474.dkr.ecr.eu-west-3.amazonaws.com
  docker push 807185093474.dkr.ecr.eu-west-3.amazonaws.com/uat-bulk-allocation-microservice:${BUILD_NUMBER}
  sed -i "s/:1.0/:$BUILD_NUMBER/g; s/accountid/807185093474/g; s/ecrrepo/uat-bulk-allocation-microservice/g" DevOps/nb-revola-bulk-allocation-deployment.yaml
else
  echo "Invalid Environment"
fi