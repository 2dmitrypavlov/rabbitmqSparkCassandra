#!/usr/bin/env bash
yum update -y
yum install -y docker
service docker start
usermod -a -G docker ec2-user
wget https://github.com/docker/compose/releases/download/1.13.0/docker-compose-`uname -s`-`uname -m`
mv docker-compose-Linux-x86_64 /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose