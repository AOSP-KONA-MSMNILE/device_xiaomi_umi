#
# Copyright (C) 2021 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Inherit from the device configuration.
$(call inherit-product, device/xiaomi/umi/device.mk)

# Inherit from the Lineage configuration.
$(call inherit-product, vendor/arrow/config/common.mk)

# Gapps
$(call inherit-product, vendor/extra/product.mk)

# Set Boot Animination Resolution
TARGET_BOOT_ANIMATION_RES := 1080

PRODUCT_NAME := arrow_umi
PRODUCT_DEVICE := umi
PRODUCT_BRAND := Xiaomi
PRODUCT_MODEL := Mi 10
PRODUCT_MANUFACTURER := Xiaomi

PRODUCT_GMS_CLIENTID_BASE := android-xiaomi

BUILD_FINGERPRINT := Xiaomi/umi/umi:12/RKQ1.211001.001/V13.0.6.0.SJBCNXM:user/release-keys
