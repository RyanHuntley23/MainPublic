#Created by Marc Huntley

import os
import PIL.Image as Image
import PIL.ImageOps as ImageOps

myroot = r'C:\Users\Ryan\Desktop\Ryan_Files\Projects\TTR\Tickets'

for root, dirs, files in os.walk(myroot):
    for x in files:
        mypicture = Image.open(os.path.join(root, x))
        mypicture = ImageOps.expand(mypicture, border=17, fill='white')
        mypicture.save(myroot + '\\Borders' + '\\' + x)
