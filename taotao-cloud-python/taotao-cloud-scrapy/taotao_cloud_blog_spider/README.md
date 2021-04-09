### taotao 爬虫模块

conda create --name python3.8 python=3.8
conda activate python3.8
conda search scrapy
conda install scrapy
conda install protego
scrapy version

#######################################3
scrapy startproject taotao_cloud_blog_spider

cd taotao_cloud_blog_spider

scrapy genspider taotao_cloud_blog blog.taotaocloud.top

scrapy crawl taotao_cloud_blog


