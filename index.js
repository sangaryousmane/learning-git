
async function doubleAndAdd(a,b){
    a = await doubleAfter1Second(a);
    b = await doubleAfter1Second(b);
    
    return a + b;
    }
    
    doubleAndAdd(1,2).then(console.log);
    
    function doubleAfter1Second(param){
        return new Promise(resolve =>{
            setTimeout(resolve(param * 2), 1000);
        });
    }