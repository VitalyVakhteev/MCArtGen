# MCArtGen
## Version 1.0.3

A Minecraft plugin that receives POST requests to generate pixel art. 

To use, run the plugin on a server (currently version 1.20.4) and send a POST request to the server with the following JSON body:

```json
{
  "image_url": "Url with the image to generate",
  "x": "0",
  "y": "96", // Minimum is 96 blocks; The maximum height the image can print completely at is 224 blocks
  "z": "0",
}
```

In return, you will get a structure resembling the image at the coordinates you provided.

## Future Improvements:
- Better performance
- Collision checks