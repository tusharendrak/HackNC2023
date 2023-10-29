const functions = require("firebase-functions");
const admin = require("firebase-admin");
const tf = require("@tensorflow/tfjs-node");
const mobilenet = require("@tensorflow-models/mobilenet");

admin.initializeApp();

let net;

/**
 * Load the MobileNet model.
 * @return {Promise<void>}
 */
async function loadModel() {
  if (!net) {
    net = await mobilenet.load();
  }
}

exports.checkForRoadImage = functions
    .runWith({memory: "2GB", timeoutSeconds: 540})
    .database.ref("uploads/{uploadId}")
    .onCreate(async (snapshot) => {
      const uploadData = snapshot.val();
      const imageUrl = uploadData.imageUrl;

      if (!imageUrl) {
        console.error("No image URL found");
        return null;
      }

      const imagePath = extractFilePathFromImageUrl(imageUrl);
      if (!imagePath) {
        console.error("Failed to extract image path from URL");
        return null;
      }

      const imageBuffer = await fetchImageFromStorage(imagePath);

      if (!await isRoad(imageBuffer)) {
        await admin.storage().bucket().file(imagePath).delete();
        await snapshot.ref.remove();
        console.log("Removed non-road image and record.");
      }
    });

/**
 * Fetches an image from Firebase Storage based on its file path.
 * @param {string} filePath - Path of the image file in Firebase Storage.
 * @return {Promise<Buffer>} - Buffer containing the image data.
 */
async function fetchImageFromStorage(filePath) {
  const bucket = admin.storage().bucket();
  const file = bucket.file(filePath);
  const contents = await file.download();
  return contents[0];
}

/**
 * Determines if the image represented by the buffer is a road.
 * @param {Buffer} imageBuffer - Buffer containing the image data.
 * @return {Promise<boolean>} - True if the image is a road, false otherwise.
 */
async function isRoad(imageBuffer) {
  await loadModel();
  const imageTensor = tf.node.decodeImage(imageBuffer, 3);
  const predictions = await net.classify(imageTensor);
  imageTensor.dispose();

  console.log(predictions); // Log predictions to inspect them

  // Check if the top prediction's label
  // contains "road" and has a confidence of more than 80%
  console.log(predictions);
  return predictions[0].className.toLowerCase()
      .includes("road") && predictions[0].probability > 0.5;
}


/**
 * Extracts the file path from a Firebase storage image URL.
 * @param {string} url - Firebase storage image URL.
 * @return {string | null} - Extracted file path or null if not found.
 */
function extractFilePathFromImageUrl(url) {
  const match = url.match(/o\/(.*?)(\?.*)?$/);
  return match ? decodeURIComponent(match[1]) : null;
}
