#!/bin/bash
#
#
# 时间
DATE=$(date '+%Y-%m-%d_%H_%M_%S')
# 备份目录
BACKUPDIR="/home/backups"
# 需要备份的目录
SORFILE=/opt
# 目标文件名
DESFILE=/home/backups/$SORFILE.$(date '+%Y-%m-%d_%H_%M_%S').zip

[ ! -d $BACKUPDIR ] && mkdir -p $BACKUPDIR
cd $BACKUPDIR

echo "start backup $SORFILE ..."
sleep 3
#echo "$DESFILE"


#tar cvf $DESFILE $SORFILE
#gzip -f .zip $DESFILE
zip -r $DESFILE $SORFILE &>/dev/null
if [ "$?" == "0" ]
then
   echo $(date +%Y-%m-%d)" zip sucess">>backup.log
else
   echo $(date +%Y-%m-%d)" zip failed">>backup.log
   exit 0
fi

# 删除3天前的备份
find $BACKUPDIR -type f -ctime +3 | xargs rm -rf
