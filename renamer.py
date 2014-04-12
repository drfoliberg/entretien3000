import re
from metadata import *


class Renamer:

    release_date_index_start = -1
    release_date_index_end = -1
    quality_index_start = -1
    quality_index_end = -1

    def get_metadata(self, filename):
        filename = self.clean(filename)
        quality = self.find_quality(filename)
        release_date = self.find_release_date(filename)

        index_title_end = (self.release_date_index_start if
            self.release_date_index_start < self.quality_index_start else
            self.quality_index_start)

        title = filename[:index_title_end]

        if title.endswith('[') or title.endswith('.'):
            title = title[:index_title_end - 1]
        m = metadata(title, release_date, quality)

        return m

    def clean(self, filename):
        filename = filename.replace(" ", ".")
        filename = filename.replace("(", "[")
        filename = filename.replace(")", "]")
        return filename

    def find_quality(self, filename):
        res = re.search('[0-9]{3,4}p', filename)
        if res is None:
            return -1
        self.quality_index_start = res.start()
        self.quality_index_end = res.end()
        return res.group()

    def find_release_date(self, filename):
        res = re.search('(.+)((19|20)[0-9]{2})', filename)
        if res is None:
            return -1
        self.release_date_index_start = res.start(2)
        self.release_date_index_end = res.end(2)
        return res.group(2)
