FROM openjdk:8

# use chinese apt mirror
COPY sources.list /etc/apt/sources.list

# delete net-tools
RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \ 
	curl \
	netcat \
&& rm -rf /var/lib/apt/lists/*

ENV HBASE_VERSION 2.2.5
# 下载hbase
wget http://hbase:hbase-2.2.5-bin.tar.gz
COPY hbase-2.2.5-bin.tar.gz /tmp/hbase.tar.gz

RUN set -x \
	&& tar -xvf /tmp/hbase.tar.gz -C /opt/ \
	&& rm /tmp/hbase.tar.gz* \
	&& ln -s /opt/hbase-$HBASE_VERSION/conf /etc/hbase \
	&& mkdir /opt/hbase-$HBASE_VERSION/logs \
	&& mkdir /hadoop-data

ENV HBASE_PREFIX=/opt/hbase-$HBASE_VERSION
ENV HBASE_CONF_DIR=/etc/hbase

ENV USER=root
ENV PATH $HBASE_PREFIX/bin/:$PATH

COPY entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

EXPOSE 16000 16010 16020 16030

ENTRYPOINT ["/entrypoint.sh"]

CMD /opt/hbase-$HBASE_VERSION/bin/hbase $HBASE_START_TYPE start

