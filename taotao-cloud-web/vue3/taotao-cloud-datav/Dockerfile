FROM node:14-alpine AS build

WORKDIR /app

COPY . /app

RUN npm install && npm run build

FROM nginx:stable-alpine

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
