# Branch Predictor Implementation

## Overview

This project implements a **branch predictor** as part of a **Computer Architecture assignment**. The goal is to design a branch predictor that operates within given **area constraints** while achieving a specified **minimum accuracy**. The predictor is tested against predefined branch instruction traces and evaluated for its performance.

## Features

✔ **Multiple Predictor Configurations** – Supports predictor sizes of **2400, 6400, 9999, and 32000 bits**.  
✔ **Custom Prediction Mechanism** – Uses tables and registers to perform **branch predictions** efficiently.  
✔ **Training Mechanism** – Updates predictor state based on actual branch outcomes.  
✔ **Performance Evaluation** – Measures accuracy over multiple test traces.  
✔ **Comparison with Machine Learning** – Predictions are compared against results from **WEKA or Caffe** machine learning models.  

## Implementation Details

The project includes four implementations for different area constraints:

- **Predictor2400.java** – Implements a branch predictor with a 2400-bit limit.
- **Predictor6400.java** – Implements a predictor with a 6400-bit limit.
- **Predictor9999.java** – Implements a predictor with a 9999-bit limit.
- **Predictor32000.java** – Implements a predictor with a 32000-bit limit.

Each predictor follows these functions:

1. **`predictor()`** – Initializes tables and registers based on size constraints.
2. **`boolean predict(long address)`** – Predicts whether a branch will be taken or not.
3. **`void train(long address, boolean outcome, boolean predict)`** – Updates predictor state based on actual outcomes.

## Prediction Accuracy

| Predictor Size | Accuracy |
|---------------|----------|
| **2400 bits** | **95.11%** |
| **6400 bits** | **96.11%** |
| **9999 bits** | **96.89%** |
| **32000 bits** | **96.18%** |

## How to Run

1. Compile the project:
   ```bash
   ant clean
   ant
   ant make-jar
   ```
2. Run the branch predictor with a trace file:
   ```bash
   java -jar jar/BranchPredictor.jar traces/trace1 2400
   ```
   - Replace `trace1` with other traces as needed.
   - Modify `2400` to test different predictor sizes.

3. Evaluate the design using:
   ```bash
   python test.py <path-to-archive>
   ```

4. Clean logs and class files:
   ```bash
   python test.py clean
   ```

## Evaluation and Machine Learning Comparison

- The accuracy of each predictor is tested across **five branch instruction traces**.
- The predictor results are compared against a **machine learning-based approach** using **WEKA or Caffe**.
- The **confusion matrix** and accuracy statistics are analyzed to determine performance.

## Future Improvements

- Experiment with **more advanced branch prediction algorithms** (e.g., **Perceptron predictors**).
- Implement a **hybrid predictor** for improved accuracy.
- Optimize **runtime efficiency** for large-scale traces.

## References

- [WEKA Machine Learning Toolkit](https://www.cs.waikato.ac.nz/ml/weka/)  
- [Caffe Deep Learning Framework](http://caffe.berkeleyvision.org/)  
- [Tejas Simulator](http://www.cse.iitd.ac.in/tejas/install.html)
- 

-------- For further details, please refer to Solution_Description.pdf file. --------
Thank You!
