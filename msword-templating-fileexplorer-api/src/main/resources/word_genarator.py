import click
from docxtpl import DocxTemplate
@click.command()
@click.option('--src', default="my_word_template.docx", help='Template name with path')
@click.option('--dest', default="generated_doc.docx", help=' Generated file name with path')
@click.option('--data', help='json data')
def convert(src, dest, data):
    """Simple program that greets NAME for a total of COUNT times."""
    print(src)
    print(dest)
    print(data)

    doc = DocxTemplate(src)
    doc.render(eval(data))
    doc.save(dest)

if __name__ == '__main__':
    convert()