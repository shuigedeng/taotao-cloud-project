package com.taotao.cloud.netty.decorator;

public class Decorator implements Component {

    private Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
	public void doSomething() {
        component.doSomething();
    }

}
