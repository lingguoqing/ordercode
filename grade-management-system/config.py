from dotenv import load_dotenv

# 加载环境变量
load_dotenv()

class Config:
    # 基本配置
    SECRET_KEY = 'your-secret-key-here'
    
    # MySQL数据库配置
    SQLALCHEMY_DATABASE_URI = (
        f"mysql+pymysql://root:123456@localhost:3306/grade_management"
        "?charset=utf8mb4"
    )
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_ENGINE_OPTIONS = {
        'pool_size': 10,
        'pool_recycle': 3600,
        'pool_pre_ping': True
    }

class DevelopmentConfig(Config):
    DEBUG = True

class ProductionConfig(Config):
    DEBUG = False

# 配置映射
config = {
    'development': DevelopmentConfig,
    'production': ProductionConfig,
    'default': DevelopmentConfig
} 