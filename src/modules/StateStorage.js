class StateStroge
{
    constructor()
    {
        const value = localStorage.getItem("state");
        if(value==null) this.state = {};
        else this.state = JSON.parse(value);
    }

    set(key,value)
    {
        this.state[key] = value;
        localStorage.setItem("state",JSON.stringify(this.state));
    }
    
    get(key)
    {
       return this.state[key];
    }

    remove(key)
    {
        const state = {};
        for(let pro in this.state)
        {
            if(pro!=key)
                state[pro] = this.state[pro];
        }
        this.state = state;
        localStorage.setItem("state", JSON.stringify(this.state));
    }
    
    has(key)
    {
        return this.get(key)==undefined;
    }
    
    setState(data)
    {
        this.state = data;
        localStorage.setItem("state",JSON.stringify(this.state));
    }

    clear()
    {
        this.state = {};
        localStorage.removeItem("state");
    }

}

const stateStroge = new StateStroge();

export default stateStroge;