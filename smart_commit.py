import os

# ---------------------------
# 1. TYPE MENU
# ---------------------------
print("\nChoose commit type:")
print("[f] feature")
print("[x] fix")
print("[r] refactor")
print("[d] docs")
print("[c] chore")

choice = input("\nType (letter): ").strip().lower()

types = {
    "f": "feature",
    "x": "fix",
    "r": "refactor",
    "d": "docs",
    "c": "chore"
}

commit_type = types.get(choice, "chore")

# ---------------------------
# 2. DESCRIPTION
# ---------------------------
description = input("Description: ").strip()

# ---------------------------
# 3. PUSH ?
# ---------------------------
push_choice = input("Push? (y/n): ").strip().lower()

# ---------------------------
# 4. MESSAGE
# ---------------------------
commit_message = f"{commit_type}: {description}"

# ---------------------------
# 5. GIT ADD + COMMIT
# ---------------------------
print("\n📦 staging...")
os.system("git add -A")

print("💬 committing...")
os.system(f'git commit -m "{commit_message}"')

# ---------------------------
# 6. PUSH OPTIONAL
# ---------------------------
if push_choice == "y":
    print("🚀 pushing...")
    os.system("git push")
    print("✅ pushed!")
else:
    print("⏸️ no push")

# ---------------------------
# 7. DONE
# ---------------------------
print("\n🔥 DONE")
print(commit_message)