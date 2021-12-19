import { message } from 'antd'

class createWebSocket {
  private readonly myUrl: string
  private ws: WebSocket | undefined
  private status: string | undefined
  private messageList: string | undefined
  private pingPong: string | undefined
  private pingInterval: NodeJS.Timeout | undefined

  constructor(url: string) {
    this.connect(url)
    this.myUrl = url
    // this.getMessage()
  }

  connect(url: string) {
    this.ws = new WebSocket(url)
    this.ws.onopen = e => {
      this.status = 'open'
      e.timeStamp
      message.info('link succeed')
      // @ts-ignore
      this.ws.send('succeed')
      this.heartCheck()
    }
  }

  //异步获取数据
  async getMessage() {
    this.messageList = ''
    await new Promise((resolve: any) => {
      // @ts-ignore
      this.ws.onmessage = e => {
        this.messageList = e.data
        resolve(this.messageList)
      }
    })
    return this.messageList
  }

  heartCheck() {
    this.pingPong = 'ping'
    this.pingInterval = setInterval(() => {
      // @ts-ignore
      if (this.ws.readyState === 1) {
        // @ts-ignore
        this.ws.send('ping')
      }
    }, 10000)

    this.pingInterval = setInterval(() => {
      if (this.pingPong === 'ping') {
        this.closeHandle('pingPong没有改为pong')
      }
    }, 20000)
  }

  closeHandle(res: any) {
    console.log(res)
    if (this.status !== 'close') {
      if (this.pingInterval !== undefined) {
        clearInterval(this.pingInterval)
        clearInterval(this.pingInterval)
      }
      this.connect(this.myUrl)
    } else {
      console.log('websocket手动关闭了，或者正在连接')
    }
  }

  //关闭连接
  close() {
    if (this.pingInterval instanceof NodeJS.Timeout) {
      clearInterval(this.pingInterval)
    }
    if (this.pingInterval instanceof NodeJS.Timeout) {
      clearInterval(this.pingInterval)
    }
    // @ts-ignore
    this.ws.send('close')
    this.status = 'close'
    // @ts-ignore
    this.ws.onclose = e => {
      console.log('close')
    }
  }
}

export default createWebSocket
