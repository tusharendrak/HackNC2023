import * as functions from "firebase-functions";
import * as admin from "firebase-admin";
import {Request, Response} from "express";
import * as cors from "cors";

admin.initializeApp();
const db = admin.database();

const corsHandler = cors({origin: true});

/**
 * Endpoint to delete nearby data.
 * @param {Request} req - The HTTP Request.
 * @param {Response} res - The HTTP Response.
 */
export const deleteNearbyData = functions.https
  .onRequest((req: Request, res: Response) => {
    corsHandler(req, res, async () => {
      if (req.method !== "POST") {
        res.status(400).send({error: "Please send a POST request"});
        return;
      }

      const mainRecordId: string = req.body.mainRecordId;
      if (!mainRecordId) {
        res.status(400).send({error: "mainRecordId not provided"});
        return;
      }

      const mainRecordSnapshot = await
      db.ref(`uploads/${mainRecordId}`).once("value");
      if (!mainRecordSnapshot.exists()) {
        res.status(404).send({error: "Main record not found."});
        return;
      }

      const {lattitude, longitude} = mainRecordSnapshot.val();
      const range = 0.0001;
      const lowerLat: number = lattitude - range;
      const upperLat: number = lattitude + range;
      const lowerLng: number = longitude - range;
      const upperLng: number = longitude + range;

      const ref = db.ref("uploads");
      const snapshot = await ref.orderByChild("lattitude")
        .startAt(lowerLat).endAt(upperLat).once("value");

      const imageUrls: string[] = [];
      const recordsToDelete: string[] = [];
      snapshot.forEach((child) => {
        const childData = child.val();
        if (childData.longitude >= lowerLng &&
            childData.longitude <= upperLng) {
          imageUrls.push(childData.imageUrl);
          recordsToDelete.push(child.key as string);
        }
      });

      for (const imageUrl of imageUrls) {
        await deleteImage(imageUrl);
      }

      for (const recordKey of recordsToDelete) {
        await ref.child(recordKey).remove();
      }

      res.send({success: true, message: "deleted successfully." +
    recordsToDelete.length + "  "});
    });
  });

/**
 * Delete an image from Firebase storage.
 * @param {string} imageUrl - Image URL.
 * @return {Promise<void>} - Resolves when the image is deleted.
 */
async function deleteImage(imageUrl: string): Promise<void> {
  const filePath: string | null = extractFilePathFromImageUrl(imageUrl);
  if (filePath) {
    const bucket = admin.storage().bucket();
    await bucket.file(decodeURIComponent(filePath)).delete();
  }
}

/**
 * Extracts the file path from a Firebase storage image URL.
 * @param {string} url - Firebase storage image URL.
 * @return {string | null} - Extracted file path or null if not found.
 */
function extractFilePathFromImageUrl(url: string): string | null {
  const match = url.match(/o\/(.*?)(\?.*)?$/);
  return match ? match[1] : null;
}
