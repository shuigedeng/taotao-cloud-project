yum -y install python38 python38-devel
yum install gcc -y

cd /opt
python3.8 -m venv py38

source /opt/py38/bin/activate

pip install --upgrade pip
pip install django
pip install werobot
pip install cryptography
pip install uwsgi

cd taotao_cloud_mp/

uwsgi --ini uwsgi.ini
# uwsgi --reload uwsgi.pid
# uwsgi --stop uwsgi.pid

openresty -p /opt/openresty -c conf/nginx.conf -t
