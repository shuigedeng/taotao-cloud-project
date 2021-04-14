const express = require("express");
const tools = require("../../model/tools");

var router = express.Router();
router.get("/", (req, res) => {
    res.send("导航列表")
})
router.get("/add", (req, res) => {
    res.render("admin/nav/add")
})
router.get("/edit", (req, res) => {
    res.send("修改导航")
})
router.post("/doAdd",tools.multer().single("pic"), (req, res) => {
    //获取表单传过来的数据    
    res.send({
        body: req.body,
        file: req.file
    });
})
router.post("/doEdit", (req, res) => {
    res.send("执行修改")
})

module.exports = router