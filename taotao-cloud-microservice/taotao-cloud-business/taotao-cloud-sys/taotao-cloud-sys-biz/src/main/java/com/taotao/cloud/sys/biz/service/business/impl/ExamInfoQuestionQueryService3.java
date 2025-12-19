package com.taotao.cloud.sys.biz.service.business.impl;

import java.util.stream.Collectors;

//复杂查询
//用函数替代变量，看到了函数式的影子。在函数式编程里面，函数有几个特点函数组合: 多个函数可以合并成一个函数。
// 引用透明: 函数的返回值只取决于输入值，也就是没有副作用。函数缓存: 函数是闭包，内部可以存数据。高阶函数: 函数可以作为参数或者返回值传递。
// 柯里化: 将接收多个参数的函数转换成一系列嵌套的单个参数的函数。函数组合和透明引用是加缓存的基础，如果没有透明引用，是加不了缓存的。
// getStudentQuestionAnswerPerQuestion 其实是传了2 个参数，一个是 examId，一个是 question。能否把函数柯里化，改
// 成 getStudentQuestionAnswerPerQuestion(examId)(question) 呢? 好处就是通过 getStudentQuestionAnswerPerQuestion(examId)
// 提前把所有 question 都查出来，再利用这个函数去筛选出单个 question 的值。
/**
 * ExamInfoQuestionQueryService3
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class ExamInfoQuestionQueryService3 {

    private ExamQuestionMapper questionMapper;
    private StudentQuestionAnswerMapper studentQuestionAnswerMapper;
    private ExamMapper examMapper;
    private StudentMapper studentMapper;

    /**
     * 获取测验和题目的正确率和参与率
     */
    public ExamDto getQuestionByExamId( String examId ) {

        ExamDto examDto = new ExamDto();

        examDto.setCorrectRate(calculateExamCorrectRate(examId));

        examDto.setSubmittedRate(calculateExamSubmittedRate(examId));

        examDto.setQuestions(getExamQuestions(examId).stream().map(question -> {

            QuestionDto questionDto = new QuestionDto();
            // 计算题目正确率
            questionDto.setCorrectRate(calculateQuestionCorrectRate(examId).apply(question));
            // 计算题目参与率
            questionDto.setSubmittedRate(calculateQuestionSubmittedRate(examId).apply(question));

            return questionDto;
        }).collect(Collectors.toList()));
        return examDto;
    }

    private double calculateExamSubmittedRate( String examId ) {
        return 1.0 * getStudentQuestionAnswers(examId).size() / getStudents(examId).size();
    }

    private long calculateExamCorrectRate( String examId ) {
        return getCorrectStudentAnswerCount(examId) / getStudentQuestionAnswers(examId).size();
    }

    private long getCorrectStudentAnswerCount( String examId ) {
        return getStudentQuestionAnswers(examId).stream().filter(StudentQuestionAnswer::isCorrect)
                .count();
    }

    private List<Student> getStudents( String examId ) {
        return studentMapper.selectByClassroom(getExam(examId).getClassroomId());
    }

    private Exam getExam( String examId ) {
        return examMapper.selectById(examId);
    }

    private List<StudentQuestionAnswer> getStudentQuestionAnswers( String examId ) {
        return studentQuestionAnswerMapper.selectByExamQuestionIds(
                getExamQuestionIds(getExamQuestions(examId)));
    }

    private static List<String> getExamQuestionIds( List<Question> questions ) {
        return questions.stream().map(Question::getId).collect(Collectors.toList());
    }

    private Function<Question, Double> calculateQuestionSubmittedRate( String examId ) {
        return question -> 1.0 * getStudentQuestionAnswerPerQuestion(examId).apply(question).size()
                / getStudents(examId).size();
    }

    private Function<Question, Double> calculateQuestionCorrectRate( String examId ) {
        return question -> 1.0 * getCorrectCountPerQuestion(examId).apply(question)
                / getAnswerCount(examId).apply(question);
    }

    private Function<Question, Integer> getAnswerCount( String examId ) {
        return question -> getStudentQuestionAnswerPerQuestion(examId).apply(question).size();

    }

    private Function<Question, Long> getCorrectCountPerQuestion( String examId ) {
        return question -> getStudentQuestionAnswerPerQuestion(examId).apply(question).stream()
                .filter(StudentQuestionAnswer::isCorrect).count();
    }

    private Function<Question, List<StudentQuestionAnswer>> getStudentQuestionAnswerPerQuestion(
            String examId ) {

        Map<String, List<StudentQuestionAnswer>> studentQuestionAnswerMap = getStudentQuestionAnswers(
                examId).stream()
                .collect(Collectors.groupingBy(StudentQuestionAnswer::getExamQuestionId));

        return question -> studentQuestionAnswerMap.getOrDefault(question.getId(),
                Collections.emptyList());
    }

    private List<Question> getExamQuestions( String examId ) {
        return questionMapper.selectByExamId(examId);
    }
}
