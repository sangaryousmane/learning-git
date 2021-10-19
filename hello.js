
class Typing{

    constructor(params) {
        this.params=params;
        this.greet=function(){
            console.log("Hello World!");
        }
        this.speak=function(){
            console.log("Yo bro, how is everything there");
        
    }
}

const c=new Typing(10);
console.log(c.params);

c.greet();
c.speak();
