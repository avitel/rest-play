FROM postgres:latest

COPY ./001_data.sql /docker-entrypoint-initdb.d/001_data.sql

ENV PGDATA=/data