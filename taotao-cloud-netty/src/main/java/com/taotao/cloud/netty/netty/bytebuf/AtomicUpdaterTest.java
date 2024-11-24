package com.taotao.cloud.netty.netty.bytebuf;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicUpdaterTest {
	public static class Person {
		volatile int age = 1;
	}

	public static void main(String[] args) {
//        Person person = new Person();
//
//        for (int i = 0; i < 10; ++i) {
//            Thread thread = new Thread(() -> {
//
//                try {
//                    Thread.sleep(20);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println(person.age++);
//            });
//
//            thread.start();
//        }

		Person person = new Person();

		AtomicIntegerFieldUpdater<Person> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.
			newUpdater(Person.class, "age");

		for (int i = 0; i < 10; ++i) {
			Thread thread = new Thread(() -> {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println(atomicIntegerFieldUpdater.getAndIncrement(person));
			});

			thread.start();
		}
	}
}


