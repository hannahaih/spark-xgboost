/*
 Copyright (c) 2014 by Contributors

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package ml.dmlc.xgboost4j.scala

import _root_.scala.collection.JavaConverters._
import ml.dmlc.xgboost4j.LabeledPoint
import ml.dmlc.xgboost4j.java.rapids.ColumnData
import ml.dmlc.xgboost4j.java.{DataBatch, XGBoostError, DMatrix => JDMatrix}

class DMatrix private[scala](private[scala] val jDMatrix: JDMatrix) {

  /**
   * init DMatrix from file (svmlight format)
   *
   * @param dataPath path of data file
   * @throws XGBoostError native error
   */
  def this(dataPath: String) {
    this(new JDMatrix(dataPath))
  }

  /**
    *  init DMatrix from Iterator of LabeledPoint
    *
    * @param dataIter An iterator of LabeledPoint
    * @param cacheInfo  Cache path information, used for external memory setting, null by default.
    * @throws XGBoostError native error
    */
  def this(dataIter: Iterator[LabeledPoint], cacheInfo: String = null) {
    this(new JDMatrix(dataIter.asJava, cacheInfo))
  }

  /**
   * create DMatrix from sparse matrix
   *
   * @param headers index to headers (rowHeaders for CSR or colHeaders for CSC)
   * @param indices Indices (colIndexs for CSR or rowIndexs for CSC)
   * @param data    non zero values (sequence by row for CSR or by col for CSC)
   * @param st      sparse matrix type (CSR or CSC)
   */
  @throws(classOf[XGBoostError])
  @deprecated
  def this(headers: Array[Long], indices: Array[Int], data: Array[Float], st: JDMatrix.SparseType) {
    this(new JDMatrix(headers, indices, data, st))
  }

  /**
   * create DMatrix from sparse matrix
   *
   * @param headers index to headers (rowHeaders for CSR or colHeaders for CSC)
   * @param indices Indices (colIndexs for CSR or rowIndexs for CSC)
   * @param data    non zero values (sequence by row for CSR or by col for CSC)
   * @param st      sparse matrix type (CSR or CSC)
   * @param shapeParam when st is CSR, it specifies the column number, otherwise it is taken as
   *                     row number
   */
  @throws(classOf[XGBoostError])
  def this(headers: Array[Long], indices: Array[Int], data: Array[Float], st: JDMatrix.SparseType,
           shapeParam: Int) {
    this(new JDMatrix(headers, indices, data, st, shapeParam))
  }

  /**
   * create DMatrix from dense matrix
   *
   * @param data data values
   * @param nrow number of rows
   * @param ncol number of columns
   */
  @throws(classOf[XGBoostError])
  def this(data: Array[Float], nrow: Int, ncol: Int) {
    this(new JDMatrix(data, nrow, ncol))
  }

  /**
   * create DMatrix from dense matrix
   *
   * @param data data values
   * @param nrow number of rows
   * @param ncol number of columns
   * @param missing the specified value to represent the missing value
   */
  @throws(classOf[XGBoostError])
  def this(data: Array[Float], nrow: Int, ncol: Int, missing: Float) {
    this(new JDMatrix(data, nrow, ncol, missing))
  }

  /**
   * create DMatrix from CUDF
   * @param cds ColumnData
   * @param gpuId gpu id to use
   * @param missing missing value
   * @throws ml.dmlc.xgboost4j.java.XGBoostError
   */
  @throws(classOf[XGBoostError])
  def this(gpuId: Int, missing: Float, cds: ColumnData*) {
    this(new JDMatrix(gpuId, missing, cds: _*))
  }

  /**
   * create DMatrix from CUDF
   * @throws ml.dmlc.xgboost4j.java.XGBoostError
   */
  @throws(classOf[XGBoostError])
  def this(cds: ColumnData*) {
    this(0, 0.0f, cds: _*)
  }

  /**
    * Append CUDF to dmatrix
    * @param cds ColumnDatas
    */
  @throws(classOf[XGBoostError])
  def appendCUDF(cds: ColumnData*): Unit = {
    jDMatrix.appendCUDF(cds: _*)
  }

  /**
   * set CUDF info of dmatrix
   * @param field label or feature
   * @param cds ColumnDatas
   */
  @throws(classOf[XGBoostError])
  def setCUDFInfo(field: String, cds: ColumnData*): Unit = {
    jDMatrix.setCUDFInfo(field, cds: _*)
  }

  /**
    * set CUDF info of dmatrix
    * @param field label or feature
    * @param cds ColumnDatas
    */
  @throws(classOf[XGBoostError])
  def appendCUDFInfo(field: String, cds: ColumnData*): Unit = {
    jDMatrix.appendCUDFInfo(field, cds: _*)
  }

  /**
   * set label of dmatrix
   *
   * @param labels labels
   */
  @throws(classOf[XGBoostError])
  def setLabel(labels: Array[Float]): Unit = {
    jDMatrix.setLabel(labels)
  }

  /**
   * set weight of each instance
   *
   * @param weights weights
   */
  @throws(classOf[XGBoostError])
  def setWeight(weights: Array[Float]): Unit = {
    jDMatrix.setWeight(weights)
  }

  /**
   * if specified, xgboost will start from this init margin
   * can be used to specify initial prediction to boost from
   *
   * @param baseMargin base margin
   */
  @throws(classOf[XGBoostError])
  def setBaseMargin(baseMargin: Array[Float]): Unit = {
    jDMatrix.setBaseMargin(baseMargin)
  }

  /**
   * if specified, xgboost will start from this init margin
   * can be used to specify initial prediction to boost from
   *
   * @param baseMargin base margin
   */
  @throws(classOf[XGBoostError])
  def setBaseMargin(baseMargin: Array[Array[Float]]): Unit = {
    jDMatrix.setBaseMargin(baseMargin)
  }

  /**
   * Set group sizes of DMatrix (used for ranking)
   *
   * @param group group size as array
   */
  @throws(classOf[XGBoostError])
  def setGroup(group: Array[Int]): Unit = {
    jDMatrix.setGroup(group)
  }

  /**
   * get label values
   *
   * @return label
   */
  @throws(classOf[XGBoostError])
  def getLabel: Array[Float] = {
    jDMatrix.getLabel
  }

  /**
   * get weight of the DMatrix
   *
   * @return weights
   */
  @throws(classOf[XGBoostError])
  def getWeight: Array[Float] = {
    jDMatrix.getWeight
  }

  /**
   * get base margin of the DMatrix
   *
   * @return base margin
   */
  @throws(classOf[XGBoostError])
  def getBaseMargin: Array[Float] = {
    jDMatrix.getBaseMargin
  }

  /**
   * Slice the DMatrix and return a new DMatrix that only contains `rowIndex`.
   *
   * @param rowIndex row index
   * @return sliced new DMatrix
   */
  @throws(classOf[XGBoostError])
  def slice(rowIndex: Array[Int]): DMatrix = {
    new DMatrix(jDMatrix.slice(rowIndex))
  }

  /**
   * get the row number of DMatrix
   *
   * @return number of rows
   */
  @throws(classOf[XGBoostError])
  def rowNum: Long = {
    jDMatrix.rowNum
  }

  /**
   * save DMatrix to filePath
   *
   * @param filePath file path
   */
  def saveBinary(filePath: String): Unit = {
    jDMatrix.saveBinary(filePath)
  }

  def getHandle: Long = {
    jDMatrix.getHandle
  }

  def delete(): Unit = {
    jDMatrix.dispose()
  }
}
