FROM debian:buster
USER root
RUN apt-get update
RUN apt-get install mariadb-server -y
COPY ./50-server.cnf /etc/mysql/mariadb.conf.d/
CMD /usr/bin/mysqld_safe
