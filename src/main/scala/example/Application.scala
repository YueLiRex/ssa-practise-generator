package example

import java.io.{BufferedWriter, File, FileWriter}

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import scala.util.Random

object Application extends App {
  val uri = getClass.getClassLoader.getResource("AWS-Certified-Solutions-Architect-Associate-Mock-Questions.pdf").toURI
  val pdf = PDDocument.load(new File(uri))
  pdf.removePage(0)
  pdf.removePage(0)
  val pdfStripper = new PDFTextStripper
  val contents = pdfStripper.getText(pdf)
  pdf.close()
  val replacedContent = contents.replaceAll("""(Get Latest & Actual Amazon Exam's Question and Answers from Passleader.                                 \nhttp://www.passleader.com  \n\d+)""", "")
  val result: Array[Array[String]] = replacedContent.split("QUESTION \\d+").map(_.trim).map(_.split("Answer").map(_.trim)).drop(1)
  val choosenQuestions = for (i <- 1 to 25) yield Random.nextInt(result.length)
  val questionPool: Array[(String, String)] = result.zipWithIndex.map { pairWithIndex =>
    val (pair, index) = pairWithIndex
    val question = s"Question $index\n ${pair(0)}"
    val answer = s"Question $index\n Answer ${pair(1)}"
    (question, answer)
  }

  val random = Random.nextInt(1000)
  val questionFile = new File(s"target/question$random.txt")
  val answerFile = new File(s"target/answer$random.txt")
  val questionWriter = new BufferedWriter(new FileWriter(questionFile))
  val answerWriter = new BufferedWriter(new FileWriter(answerFile))
  choosenQuestions.foreach { n =>
    val theQuestion = questionPool(n)
    questionWriter.write(theQuestion._1+"\n\n")
    answerWriter.write(theQuestion._2+"\n\n")
  }
  questionWriter.close()
  answerWriter.close()


}

