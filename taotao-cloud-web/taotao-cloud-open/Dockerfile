FROM node:alpine as builder

WORKDIR /root

COPY . /root

RUN apk --update add --no-cache ca-certificates curl openssl binutils xz autoconf automake gcc libtool \
    && ZLIB_URL="https://archive.archlinux.org/packages/z/zlib/zlib-1%3A1.2.9-1-x86_64.pkg.tar.xz" \
    && ZLIB_SHA256=bb0959c08c1735de27abf01440a6f8a17c5c51e61c3b4c707e988c906d3b7f67 \
    && strip /usr/glibc-compat/lib/libgcc_s.so.* /usr/glibc-compat/lib/libstdc++.so* \
    && curl -Ls ${ZLIB_URL} -o /tmp/libz.tar.xz \
    && echo "${ZLIB_SHA256}  /tmp/libz.tar.xz" | sha256sum -c - \
    && mkdir /tmp/libz \
    && tar -xf /tmp/libz.tar.xz -C /tmp/libz \
    && mv /tmp/libz/usr/lib/libz.so* /usr/glibc-compat/lib \
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
