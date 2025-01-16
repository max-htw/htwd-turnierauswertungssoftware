FROM node:lts-alpine AS build
WORKDIR /node/src/app
COPY . /node/src/app/
RUN npm install
RUN npm run build

FROM node:lts-alpine AS prod
WORKDIR /node
COPY --from=build /node/src/app/.output /node
EXPOSE 3000
CMD ["nodejs","./server/index.mjs"]
