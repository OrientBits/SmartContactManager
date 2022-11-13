
console.log("this is js script file")

const toggleSidebar = () =>{
    if ( $(".sidebar").is(":visible")){
        $(".sidebar").css("display","none")
        $(".content").css("margin-left","0%")
    }else{
        $(".sidebar").css("display","lock")
        $(".content").css("margin-left","20%")
    }
}
