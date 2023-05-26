FROM node:18.15.0 AS builder

WORKDIR /app

COPY src /app/src
COPY package.json /app/package.json
# modified tsconfig.jso
COPY tsconfig.json /app/tsconfig.json
# modified angular.jso
COPY angular.json /app/angular.json
COPY tsconfig.app.json /app/tsconfig.app.json

RUN npm install
RUN npm install -g @angular/cli@latest

RUN ng build
# RUN npm run build

FROM nginx

COPY --from=builder /app/dist/test /usr/share/nginx/html

EXPOSE 80