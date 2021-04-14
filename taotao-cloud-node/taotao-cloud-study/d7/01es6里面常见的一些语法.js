/*
1. let const 模板字符串
2. 箭头函数 
3. 对象、属性的简写 
4. 模板字符串 
5. Promise

*/

// 1. let const的使用     let和 var是一样的用来定义变量

//   let是一个块作用域
//   let a=123;  

// if(true){
//     let a=123;
// }
// console.log(a);

/*常量
    const PI=3.14159;

    PI=3;

    console.log(PI);
 */

/*模板字符串
    
    var name='张三';
    var age=20;
    console.log(name+'的年龄是'+age);

  
    var name='张三';
    var age=20;
    console.log(`${name}的年龄是${age}`);



 */

/*方法的简写 属性的简写

    var name='zhangsan';
    var app={
        name:name
    }

    console.log(app.name);

 属性的简写
    var name='zhangsan';
    var app={
        name
    }

    console.log(app.name);



方法的简写
    var name='zhangsan';
    var app={
        name,
        run(){

            console.log(`${this.name}在跑步`);
        }
    }

    app.run();



 */

/*
箭头函数   this指向上下文

 setTimeout(function (){

    console.log('执行');
 },1000)


 setTimeout(()=>{

     console.log('执行');
 },1000)

* */

/*回调函数 获取异步方法里面的数据
     function getData(callbck){

        //ajax
        setTimeout(function(){
            var name='张三';
            callbck(name);

        },1000);

     }

     //外部获取异步方法里面的数据

     getData(function(data){
         console.log(data+'111');
     })
* */

/*Promise来处理异步  resolve 成功的回调函数   reject失败的回调函数

 
 var p=new Promise(function(resolve,reject){
     //ajax
     setTimeout(function(){
         var name='张三';

         if(Math.random()<0.7){
            resolve(name);

         }else{
            reject('失败');
         }


     },1000);
 })


 p.then((data)=>{

    console.log(data);
 })

* */

/*

获取异步方法里面的数据

function getData(resolve,reject){

    //ajax
    setTimeout(function(){
        var name='张三';
        resolve(name);

    },1000);

}

var p=new Promise(getData);


p.then((data)=>{

    console.log(data);
})
*/

//  var p=new Promise(function(resolve,reject){
//     setTimeout(function(){
//         var name='张三 11';
//         resolve(name);
//     },1000);
//  })

//  p.then(function(data){
//     console.log(data);
//  })

function getData(resolve, reject) {
    //ajax
    setTimeout(function () {
        var name = '张三 222';
        resolve(name);

    }, 1000);

}

var p = new Promise(getData);

p.then((data) => {
    console.log(data);
})
