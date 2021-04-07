import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import Home from "../home";

const Routers: React.FC = () => {
  return (
      <BrowserRouter>
        <Switch>
          <Route exact path='/' component={Home} />
          {/*<Route path="/list/:defaultActiveKey" component={List}/>*/}
          {/*<Route path="/detail/:id" component={Detail}/>*/}
          {/*
			 <Route path="/cart/:id" component={Cart}/>
		  */}
        </Switch>
      </BrowserRouter>
  )
}

export default Routers;
