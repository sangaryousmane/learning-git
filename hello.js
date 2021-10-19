
class Typing{

    constructor(params) {
        this.params=params;
        this.greet=function(){
            console.log("Hello World!");
        }
        
    }
}

const c=new Typing(10);
console.log(c.params);

c.greet();