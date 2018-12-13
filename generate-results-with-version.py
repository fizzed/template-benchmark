from tempfile import NamedTemporaryFile
import shutil
import csv
from xml.etree import ElementTree
import subprocess


with open('pom.xml', 'rt') as f:
    groups = ElementTree.parse(f)

root = groups.getroot()
tree = root.findall('.//')
tree = tree[6].findall('.//')
versions = {}
for node in tree:
    key = node.tag[node.tag.index("}") + 1:]
    versions[key] = node.text

filename = 'results.csv'
tempfile = NamedTemporaryFile(mode='w', delete=False)

fields = ['Benchmark', 'Mode', 'Threads', 'Samples', 'Score', 'Score Error (99.9%)', 'Unit']

with open(filename, 'r') as csvfile:#, tempfile:
    reader = csv.DictReader(csvfile, fieldnames=fields, delimiter=',')
    writer = csv.DictWriter(tempfile, fieldnames=fields)
    for row in reader:
      if row['Benchmark'] != 'Benchmark':
        key = "%s.version" % (row['Benchmark'].lower())
        version = versions[key]
        row['Benchmark'] = row['Benchmark'] + " " + version
      writer.writerow(row)

shutil.move(tempfile.name, filename)
exit()
