import os

def get_system_drive_path():
    system_drive = os.getenv("SystemDrive")
    if system_drive:
        path_to_c = system_drive + "\\"
        print(f"Caminho até o disco C: {path_to_c}")
    else:
        print("Não foi possível obter o caminho até o disco C:")

get_system_drive_path()
