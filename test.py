import random
import unittest
from renamer import *
from metadata import *


class Tests(unittest.TestCase):

    def setUp(self):
        renamer = Renamer()
        self.test1 = renamer.get_metadata("1984[1956][1080p].mkv")
        self.test2 = renamer.get_metadata("1984[1080p][1956].mkv")
        self.test3 = renamer.get_metadata("1984 1956.mkv")
        self.test4 = renamer.get_metadata("1984 1080p.mkv")
        self.test5 = renamer.get_metadata("The.Name.Of.The.Rose.1986.720p.BluRay.x264.anoXmous.mkv")

    def test_release_date(self):
        self.assertEqual(self.test1.release_date, "1956")
        self.assertEqual(self.test2.release_date, "1956")
        self.assertEqual(self.test3.release_date, "1956")
        self.assertEqual(self.test4.release_date, -1)
        self.assertEqual(self.test5.release_date, "1986")
        
    def test_quality(self):
        self.assertEqual(self.test1.quality, "1080p")
        self.assertEqual(self.test2.quality, "1080p")
        self.assertEqual(self.test3.quality, -1)
        self.assertEqual(self.test4.quality, "1080p")
        self.assertEqual(self.test5.quality, "720p")

    def test_title(self):
        self.assertEqual(self.test1.title, "1984")
        self.assertEqual(self.test2.title, "1984")
        self.assertEqual(self.test3.title, "1984")
        self.assertEqual(self.test4.title, "1984")
        self.assertEqual(self.test5.title, "The.Name.Of.The.Rose")


if __name__ == '__main__':
    unittest.main()
