# MCArtGen
<img width="847" height="471" alt="image" src="https://github.com/user-attachments/assets/fdd42f27-9310-490c-a40e-5fbadf9131a5" />

## Version 1.0.3.2

A Minecraft plugin that receives POST requests to generate pixel art. 

To use, run the plugin on a Spigot server (currently version 1.20.6) and send a POST request to the :8001/upload path of the server with the following JSON body:

```json
{
  "image_url": "Url with the image to generate",
  "x": "0",
  "y": "96", // Minimum is 96 blocks; The maximum height the image can print completely at is 224 blocks
  "z": "0",
}
```

For example, if hosted locally, you can send it to http://localhost:8001/upload.

In return, you will get a structure resembling the image at the coordinates you provided.

## Future Improvements:
- Better performance
- Collision checks
