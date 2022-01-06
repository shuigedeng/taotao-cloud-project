FROM node:alpine as builder

WORKDIR /root

COPY . /root

RUN apk --update add --no-cache ca-certificates curl openssl binutils xz autoconf automake gcc zlib-dev libtool \
    && ZLIB_URL="https://archive.archlinux.org/packages/z/zlib/zlib-1%3A1.2.11-3-x86_64.pkg.tar.xz" \
    && ZLIB_SHA256=bb0959c08c1735de27abf01440a6f8a17c5c51e61c3b4c707e988c906d3b7f67 \
    && curl -Ls ${ZLIB_URL} -o /tmp/libz.tar.xz \
    && mkdir /tmp/libz \
    && tar -xf /tmp/libz.tar.xz -C /tmp/libz \
    && cd /tmp/libz \
    && pwd \
    && ls -l \
    && ./configure  \
    && make  \
    && make install  \
    && apk del binutils

RUN yarn

RUN yarn build

# Second stage: minimal runtime environment
FROM openresty/openresty:alpine

ENV GATEWAY_URL http://127.0.0.1:8080

# copy dist from the first stage
COPY --from=builder /root/dist /usr/share/nginx/html

# copy nginx.conf
COPY nginx.conf /usr/local/openresty/nginx/conf/nginx.conf

# expose port
EXPOSE 80
