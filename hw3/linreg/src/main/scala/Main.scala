import au.com.bytecode.opencsv.CSVWriter
import breeze.linalg.{DenseMatrix, DenseVector, csvread, csvwrite}
import breeze.numerics.pow
import breeze.stats.mean
import breeze.stats.regression.leastSquares

import java.io.{File}
import java.io.{BufferedWriter, FileWriter}
import java.util.logging.{FileHandler, Logger, SimpleFormatter}
import scala.util.Random


object Main extends App{
  System.setProperty(
    "java.util.logging.SimpleFormatter.format",
    "%1$tF %1$tT %4$s %5$s%6$s%n"
  )

  val logger = Logger.getLogger("Linreg logger")
  val handler = new FileHandler("logs.txt")

  logger.addHandler(handler)
  val formatter = new SimpleFormatter()
  handler.setFormatter(formatter)


  def rmse(true_values: DenseVector[Double], predicted_values: DenseVector[Double]):Double = {
    // Root Mean Square Error
    var tmp = true_values - predicted_values
    tmp :*= tmp
    var tmp_mean : Double = mean(tmp)
    tmp_mean = pow(tmp_mean, 0.5)
    return tmp_mean
  }
  logger.info(f"Input arguments")
  if (args.length != 3) {
    println("ATTENTION! Required 3 arguments:\nTrain dataset csv file path;\nTest dataset csv file path;\nTest predictons file path")
  }

  val trainFileName = args(0)
  val testFileName = args(1)
  val outputFileName = args(2)
  logger.info(f"inputed arguments:\n$trainFileName\n$testFileName\n$outputFileName")

  val train_file: File = new File(trainFileName)
  val test_file: File = new File(testFileName)

  val train_data: DenseMatrix[Double] = csvread(train_file, skipLines = 1)
  val test_data: DenseMatrix[Double] = csvread(test_file, skipLines = 1)

  val train_m = train_data.toDenseMatrix
  val test_m = test_data.toDenseMatrix

  // Задание индексов для выделение валидационного сета
  val (train_indexes, validation_indexes) = Random.shuffle(List.range(1, train_m.rows)).splitAt((0.85 * train_data.rows).toInt)
  val train_m_ = train_data(train_indexes, ::).toDenseMatrix
  val train_m_validation = train_data(validation_indexes, ::).toDenseMatrix

  val y_train = train_data(train_indexes, -1).toDenseVector
  val y_validation = train_data(validation_indexes, -1).toDenseVector
  val y_test: DenseVector[Double] = test_m(::, -1)
  logger.info("Data has been read successfully")

  // вектор единиц которые нужны для свободного члена f(x) = a_0 * 1 + a_1 * x_1 + ... + a_n * x_n
  val ones_train = DenseMatrix.fill[Double](train_m_.rows, 1)(1)
  val ones_valid = DenseMatrix.fill[Double](train_m_validation.rows, 1)(1)
  val ones_test = DenseMatrix.fill[Double](test_m.rows, 1)(1)

  // добавим вектор единиц к матрице признаков
  val X_train = DenseMatrix.horzcat(
    ones_train,
    train_m_(::, 0 until train_m_.cols - 1)
  )
  val X_valid= DenseMatrix.horzcat(
    ones_valid,
    train_m_validation(::, 0 until train_m_validation.cols - 1).toDenseMatrix)
  val X_test = DenseMatrix.horzcat(
    ones_test,
    test_m(::, 0 until test_data.cols - 1))

  logger.info("Data has been prepared correctly")

  logger.info("Solving least squares problem")
 // подберем коэффициенты регрессии с помощью метода наименьших квадратов
  val fit = leastSquares(X_train, y_train)
  logger.info("Making predictions")
  // выполним предсказания путем умножения матрицы объекты-признаки на вектор коэффициентов
  val predict_train = X_train * fit.coefficients
  val predict_validation = X_valid * fit.coefficients
  val predict_test = X_test * fit.coefficients
  logger.info("Evaluating metrics")
  val result_train = rmse(y_train, predict_train)
  val result_valid = rmse(y_validation, predict_validation)
  val result_test = rmse(y_test, predict_test)

  logger.info(s"Train RMSE=$result_train Validation RMSE=$result_valid Test RMSE=$result_test")
  println(s"Train RMSE=$result_train Validation RMSE=$result_valid Test RMSE=$result_test")

  // Сохранение результатов
  val results_file: File = new File(outputFileName)
  csvwrite(results_file, predict_test.toDenseMatrix.reshape(predict_test.length, 1))
  logger.info(s"Finish your results placed in $outputFileName (target folder is default)")

}

