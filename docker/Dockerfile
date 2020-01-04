#
# Dockerfile for Capanda
#

FROM openjdk:12-jdk-oraclelinux7

# Install libpcap.
RUN yum install -y libpcap git net-tools tcpdump
RUN yum clean all

ENTRYPOINT ["/bin/sh", "src/run.sh"]
