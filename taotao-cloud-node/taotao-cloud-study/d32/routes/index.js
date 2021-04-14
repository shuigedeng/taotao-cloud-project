const express = require("express");

var router = express.Router()

router.get("/",(req,res)=>{
   res.send("首页")
})
 
 module.exports = router