// package com.taotao.cloud.apt.lombok;
//
// import com.google.auto.service.AutoService;
// import com.sun.source.tree.Tree;
// import com.sun.tools.javac.api.JavacTrees;
// import com.sun.tools.javac.code.Flags;
// import com.sun.tools.javac.code.Type;
// import com.sun.tools.javac.processing.JavacProcessingEnvironment;
// import com.sun.tools.javac.tree.JCTree;
// import com.sun.tools.javac.tree.TreeMaker;
// import com.sun.tools.javac.tree.TreeTranslator;
// import com.sun.tools.javac.util.Context;
// import com.sun.tools.javac.util.List;
// import com.sun.tools.javac.util.ListBuffer;
// import com.sun.tools.javac.util.Name;
// import com.sun.tools.javac.util.Names;
//
// import javax.annotation.processing.AbstractProcessor;
// import javax.annotation.processing.Messager;
// import javax.annotation.processing.ProcessingEnvironment;
// import javax.annotation.processing.Processor;
// import javax.annotation.processing.RoundEnvironment;
// import javax.annotation.processing.SupportedAnnotationTypes;
// import javax.annotation.processing.SupportedSourceVersion;
// import javax.lang.model.SourceVersion;
// import javax.lang.model.element.Element;
// import javax.lang.model.element.TypeElement;
// import javax.tools.Diagnostic;
// import java.util.Set;
//
// /**
//  * 自定义注解处理类,继承AbstractProcessor，重写init和process方法
//  *
//  */
// @AutoService(Processor.class)
// @SupportedAnnotationTypes("com.taotao.cloud.apt.lombok.MySetterGetter")
// @SupportedSourceVersion(SourceVersion.RELEASE_8)
// public class MySetterGetterProcessor extends AbstractProcessor {
//
// 	private JavacTrees javacTrees;
//
// 	private TreeMaker treeMaker;
//
// 	private Names names;
//
// 	private Messager messager;
//
// 	/**
// 	 * 从Context中初始化JavacTrees，TreeMaker，Names
// 	 *
// 	 * @param processingEnv
// 	 */
// 	@Override
// 	public synchronized void init(ProcessingEnvironment processingEnv) {
// 		messager.printMessage(Diagnostic.Kind.ERROR, "========================>init");
// 		super.init(processingEnv);
// 		Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
// 		javacTrees = JavacTrees.instance(processingEnv);
// 		treeMaker = TreeMaker.instance(context);
// 		names = Names.instance(context);
// 	}
//
// 	@Override
// 	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
// 		messager.printMessage(Diagnostic.Kind.ERROR, "========================>process");
// 		messager.printMessage(Diagnostic.Kind.NOTE, "source version -- " + getSupportedSourceVersion());
// 		// 获取注解类的集合，之后依次去处理
// 		Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(MySetterGetter.class);
// 		for (Element element : set) {
// 			// 获取当前类的抽象语法树
// 			JCTree tree = javacTrees.getTree(element);
// 			// 获取抽象语法树的所有节点
// 			// Visitor 抽象内部类，内部定义了访问各种语法节点的方法
// 			tree.accept(new TreeTranslator() {
// 				@Override
// 				public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
// 					jcClassDecl.defs.stream()
// 						// 过滤，只处理变量类型
// 						.filter(it -> it.getKind().equals(Tree.Kind.VARIABLE))
// 						// 类型强转
// 						.map(it -> (JCTree.JCVariableDecl) it)
// 						.forEach(it -> {
// 							System.out.println(it.toString());
// 							// 添加get方法
// 							jcClassDecl.defs = jcClassDecl.defs.prepend(genGetterMethod(it));
// 							// 添加set方法
// 							jcClassDecl.defs = jcClassDecl.defs.prepend(genSetterMethod(it));
// 						});
//
// 					super.visitClassDef(jcClassDecl);
// 				}
// 			});
//
// 		}
// 		return true;
// 	}
//
// 	/**
// 	 * 生成get方法
// 	 *
// 	 * @param jcVariableDecl
// 	 * @return
// 	 */
// 	private JCTree.JCMethodDecl genGetterMethod(JCTree.JCVariableDecl jcVariableDecl) {
// 		// 生成return语句，return this.xxx
// 		JCTree.JCReturn returnStatement = treeMaker.Return(
// 			treeMaker.Select(
// 				treeMaker.Ident(names.fromString("this")),
// 				jcVariableDecl.getName()
// 			)
// 		);
//
// 		ListBuffer<JCTree.JCStatement> statements = new ListBuffer<JCTree.JCStatement>().append(returnStatement);
//
// 		// public 方法访问级别修饰
// 		JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
// 		// 方法名 getXXX ，根据字段名生成首字母大写的get方法
// 		Name getMethodName = createGetMethodName(jcVariableDecl.getName());
// 		// 返回值类型，get类型的返回值类型与字段类型一致
// 		JCTree.JCExpression returnMethodType = jcVariableDecl.vartype;
// 		// 生成方法体
// 		JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
// 		// 泛型参数列表
// 		List<JCTree.JCTypeParameter> methodGenericParamList = List.nil();
// 		// 参数值列表
// 		List<JCTree.JCVariableDecl> parameterList = List.nil();
// 		// 异常抛出列表
// 		List<JCTree.JCExpression> throwCauseList = List.nil();
//
// 		// 生成方法定义树节点
// 		return treeMaker.MethodDef(
// 			// 方法访问级别修饰符
// 			modifiers,
// 			// get 方法名
// 			getMethodName,
// 			// 返回值类型
// 			returnMethodType,
// 			// 泛型参数列表
// 			methodGenericParamList,
// 			//参数值列表
// 			parameterList,
// 			// 异常抛出列表
// 			throwCauseList,
// 			// 方法默认体
// 			body,
// 			// 默认值
// 			null
// 		);
//
// 	}
//
// 	/**
// 	 * 生成set方法
// 	 *
// 	 * @param jcVariableDecl
// 	 * @return
// 	 */
// 	private JCTree.JCMethodDecl genSetterMethod(JCTree.JCVariableDecl jcVariableDecl) {
// 		// this.xxx=xxx
// 		JCTree.JCExpressionStatement statement = treeMaker.Exec(
// 			treeMaker.Assign(
// 				treeMaker.Select(
// 					treeMaker.Ident(names.fromString("this")),
// 					jcVariableDecl.getName()
// 				),
// 				treeMaker.Ident(jcVariableDecl.getName())
// 			)
// 		);
//
// 		ListBuffer<JCTree.JCStatement> statements = new ListBuffer<JCTree.JCStatement>().append(statement);
//
// 		// set方法参数
// 		JCTree.JCVariableDecl param = treeMaker.VarDef(
// 			// 访问修饰符
// 			treeMaker.Modifiers(Flags.PARAMETER, List.nil()),
// 			// 变量名
// 			jcVariableDecl.name,
// 			//变量类型
// 			jcVariableDecl.vartype,
// 			// 变量初始值
// 			null
// 		);
//
// 		// 方法访问修饰符 public
// 		JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
// 		// 方法名(setXxx)，根据字段名生成首选字母大写的set方法
// 		Name setMethodName = createSetMethodName(jcVariableDecl.getName());
// 		// 返回值类型void
// 		JCTree.JCExpression returnMethodType = treeMaker.Type(new Type.JCVoidType());
// 		// 生成方法体
// 		JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
// 		// 泛型参数列表
// 		List<JCTree.JCTypeParameter> methodGenericParamList = List.nil();
// 		// 参数值列表
// 		List<JCTree.JCVariableDecl> parameterList = List.of(param);
// 		// 异常抛出列表
// 		List<JCTree.JCExpression> throwCauseList = List.nil();
// 		// 生成方法定义语法树节点
// 		return treeMaker.MethodDef(
// 			// 方法级别访问修饰符
// 			modifiers,
// 			// set 方法名
// 			setMethodName,
// 			// 返回值类型
// 			returnMethodType,
// 			// 泛型参数列表
// 			methodGenericParamList,
// 			// 参数值列表
// 			parameterList,
// 			// 异常抛出列表
// 			throwCauseList,
// 			// 方法体
// 			body,
// 			// 默认值
// 			null
// 		);
//
// 	}
//
// 	/**
// 	 * 生成get方法名称
// 	 *
// 	 * @param variableName
// 	 * @return
// 	 */
// 	private Name createGetMethodName(Name variableName) {
// 		String fieldName = variableName.toString();
// 		return names.fromString("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
//
// 	}
//
// 	/**
// 	 * 生成set方法名称
// 	 *
// 	 * @param variableName
// 	 * @return
// 	 */
// 	private Name createSetMethodName(Name variableName) {
// 		String fieldName = variableName.toString();
// 		System.out.println(fieldName.substring(1));
// 		return names.fromString("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
//
// 	}
//
// }
