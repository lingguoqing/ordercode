import os
import mysql.connector


def get_db_connection():
    return mysql.connector.connect(
        host=os.environ.get("MYSQL_HOST", "localhost"),
        port=int(os.environ.get("MYSQL_PORT", "3306")),
        user=os.environ.get("MYSQL_USER", "root"),
        password=os.environ.get("MYSQL_PASSWORD", "123456"),
        database=os.environ.get("MYSQL_DB", "classroom_reservation"),
        autocommit=False,
        use_pure=True,
    )


