
class Typing{

    constructor(params) {
        this.params=params;
        this.greet=function(){
            console.log("Hello World!");
        }


        this.talk=function(){
            console.log("Hee Hee Hee!");
        }

        this.country=function(){
            console.log("I am a proud Guinea-Liberian");
        }

        this.speak=function(){
            console.log("Yo bro, how is everything over there");
	}


    }
}

const c;
c=new Typing(10);
console.log(c.params);

c.greet();
c.talk();
c.country();
c.speak();



