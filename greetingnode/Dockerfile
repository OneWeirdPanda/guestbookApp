# Version 0.0.1

FROM node:boron

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

RUN npm install

COPY server.js /usr/src/app/

EXPOSE 8081

CMD [ "npm", "start" ]