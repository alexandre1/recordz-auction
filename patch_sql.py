import re

def clean_and_fix_schema(input_filename, output_filename):
    with open(input_filename, 'rb') as f:
        content = f.read()

    # Define exact search-and-replace configurations for the ALTER TABLE lines
    replacements = {
        # 1. Enchere table
        r"ALTER TABLE `enchere`[\s\n]+ADD PRIMARY KEY \(`id_enchere`,[^)]+\)": 
        "ALTER TABLE `enchere` \n  ADD PRIMARY KEY (`id_enchere`)",

        # 2. Condition payement table
        r"ALTER TABLE `condition_payement_libelle_langue`[\s\n]+ADD PRIMARY KEY \(`ref_condition_payement`,[^)]+\)": 
        "ALTER TABLE `condition_payement_libelle_langue` \n  ADD PRIMARY KEY (`ref_condition_payement`)",

        # 3. Categorie libelle langue table
        r"ALTER TABLE `categorie_libelle_langue`[\s\n]+ADD PRIMARY KEY \(`ref_categorie`,[^)]+\)": 
        "ALTER TABLE `categorie_libelle_langue` \n  ADD PRIMARY KEY (`ref_categorie`)",

        # 4. Langue table
        r"ALTER TABLE `langue`[\s\n]+ADD PRIMARY KEY \(`id_langue`,[^)]+\)": 
        "ALTER TABLE `langue` \n  ADD PRIMARY KEY (`id_langue`)",

        # 5. Libelle table
        r"ALTER TABLE `libelle`[\s\n]+ADD PRIMARY KEY \(`id_libelle`,[^)]+\)": 
        "ALTER TABLE `libelle` \n  ADD PRIMARY KEY (`id_libelle`)",

        # 6. Pays table
        r"ALTER TABLE `pays`[\s\n]+ADD PRIMARY KEY \(`id_pays`,[^)]+\)": 
        "ALTER TABLE `pays` \n  ADD PRIMARY KEY (`id_pays`)",

        # 7. Pays present table
        r"ALTER TABLE `pays_present`[\s\n]+ADD PRIMARY KEY \(`id_pays_present`,[^)]+\)": 
        "ALTER TABLE `pays_present` \n  ADD PRIMARY KEY (`id_pays_present`)"
    }

    patched = content
    for pattern, replacement in replacements.items():
        patched = re.sub(pattern, replacement, patched)

    # Safety clean-up: Remove any accidental duplicate primary keys injected nearby
    patched = re.sub(r"ADD PRIMARY KEY \(`[^`]+`\),\s*\n\s*ADD PRIMARY KEY", "ADD PRIMARY KEY", patched)

    with open(output_filename, 'wb') as f:
        f.write(patched)
    print("Clean single-column Primary Key schema built successfully.")
    return True

if __name__ == "__main__":
    clean_and_fix_schema("recordz_correction.sql", "recordz_patched.sql")
