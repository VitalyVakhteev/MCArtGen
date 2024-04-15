# MCArtGen
## Version 1.0.3

A Minecraft plugin that receives POST requests to generate pixel art. 

Run the plugin on a server (currently version 1.20.4) and send a POST request to the server with the following JSON body:

```json
{
  "image_url": "Url with the image to generate",
  "x": "0",
  "y": "96", // Minimum is 96 height
  "z": "0",
}
```

## Future Improvements:
- Better performance
- Collision checks