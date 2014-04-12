class metadata:

    title = ""
    release_date = ""
    quality = ""

    def __init__(self, title, release_date, quality):
        self.title = title
        self.release_date = release_date
        self.quality = quality

    def tostring(self):
        cleaned = self.title
        if self.release_date != -1:
            cleaned = cleaned + "[{}]".format(self.release_date)
        if self.quality != -1:
            cleaned = cleaned + "[{}]".format(self.quality)
        return cleaned
