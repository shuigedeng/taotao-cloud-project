const SMSClient = require('@alicloud/sms-sdk');

module.exports = app => {
  const { CHAR, ENUM, Op } = app.Sequelize;

  // 验证码
  const SmsCode = app.model.define('smsCode', {
    phone: { type: CHAR(11), allowNull: false },
    code: {
      type: CHAR(6),
      allowNull: false,
      defaultValue: () => { // 生成6位数字的验证码
        let code = '';
        for (let i = 0; i < 6; i++) {
          const num = Math.floor(Math.random() * 10);
          code += num;
        }
        return code;
      },
    },
    // 状态
    status: {
      type: ENUM,
      values: [ 'unused', 'used' ],
      allowNull: false,
      defaultValue: 'unused',
    },
  });

  // 发送
  SmsCode.send = async function(phone) {
    const {
      accessKeyId, secretAccessKey, SignName, TemplateCode,
    } = app.config.thirdParty.aliyunSms;
    const smsClient = new SMSClient({ accessKeyId, secretAccessKey });
    // 生成6位数字的验证码
    let code = '';
    for (let i = 0; i < 6; i++) {
      const num = Math.floor(Math.random() * 10);
      code += num;
    }
    try {
      const res = await smsClient.sendSMS({
        PhoneNumbers: phone,
        SignName,
        TemplateCode,
        TemplateParam: JSON.stringify({ code }),
      });
      const { Code } = res;
      if (Code === 'OK') {
        // 处理返回参数
        await this.create({ phone, code });
      }
    } catch (e) {
      // empty
    }
  };

  // 验证
  SmsCode.verify = async function(phone, code) {
    const foundSmsCode = await this.find({
      where: {
        phone,
        code,
        status: 'unused',
        createdAt: {
          [Op.lte]: Number(new Date()) + 5 * 60 * 1000, // 5分钟有效期
        },
      },
    });
    if (foundSmsCode) await foundSmsCode.update({ status: 'used' });
    return !!foundSmsCode;
  };

  return SmsCode;
};
