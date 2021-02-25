package com.taotao.cloud.scala.p5.generic

/*
	scala版本的泛型方法
	scala中的泛型方法定义，在参数列表之前，方法名称之后，通过[]来进行定义
	scala来完成java中的泛型限定：
					java                         scala
		上限限定：  <T extends 类型>           [T <: 类型]
		下限限定：  <T super 类型>             [T >: 类型] (了解)
		视图界定        ×                       T <% 类型
	分析一下：最开始，使用scala中的上限限定，来进行处理，Array[Integer]通过了编译，但是Array[Double]没有通过编译
	第二步，我们将Array[Double]注释，完成的运行，
	第三步，将Array[Integer]中的类型转化为scala中的通过Int，发现和最开始报一样的错误，说类型服务完成匹配，或者调用
	第四步，将scala重点额上限限定，秀定位视图界定，即将<:,替换成了<%,通过编译，完成运行。

	主要原因，在于scala中的Int，Double其实并没有真正地扩展Comparable这个类，自然就无法完成编译工作
	但是我们将上限限定，修订为视图界定，就可以，这是为什么？其实在Int和Double的内部完成了类型的增强或者转换。
	内部通过后面要给大家讲解的隐式转换，隐式将Int和Double分别转换为RichInt和RichDouble，而这两个类都实现了Comparable或者
	Ordered，因此通过视图界定就可以。这个过程没有我们手动的参数，全程都是scala帮我们来实现的。

 */
object GenericMethodOps {
	def main(args: Array[String]): Unit = {
		val array = Array[Int](3, -6, 7, 1, 0, 9, 4, 2)
		val dArray = Array[Double](3d, -6d, 7d, 1d, 0d, 9d, 4d, 2d)
		System.out.println("排序前的数组array：" + array.mkString("[", ", ", "]"))
		System.out.println("排序前的数组dArray：" + dArray.mkString("[", ", ", "]"))
		//排序

		insertSort(array, true)
		insertSort(dArray, false)
		System.out.println("排序后的数组array：" + array.mkString("[", ", ", "]"))
		System.out.println("排序后的数组dArray：" + dArray.mkString("[", ", ", "]"))
	}


	/*
	for(int i = 1; i < array.length; i++) {
            for(int j = i; j > 0; j--) {
                if(asc) {
                    if (array[j].compareTo(array[j - 1]) < 0) {
                        swap(array, j, j - 1);
                    }
                } else {
                    if (array[j].compareTo(array[j - 1]) > 0) {
                        swap(array, j, j - 1);
                    }
                }
            }
        }
	 */
	def insertSort[T <% Comparable[T]](array: Array[T], asc: Boolean = true): Unit = {
		for(i <- 1 until array.length) {
			for(j <- 1 to i reverse) {
				if(asc) {
					if (array(j).compareTo(array(j - 1)) < 0) {
						swap(array, j, j - 1);
					}
				} else {
					if (array(j).compareTo(array(j - 1)) > 0) {
						swap(array, j, j - 1);
					}
				}
			}
		}
	}

	/*
	private <T> void swap(T[] array, int i, int j) {
        T tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
	 */

	def swap[T](array:Array[T], i:Int, j:Int): Unit = {
		val tmp: T = array(i)
		array(i) = array(j)
		array(j) = array(i)
	}
}
